import type {DocumentNode} from "graphql/language";
import {useApolloClient, useMutation} from "@vue/apollo-composable";
import type {Connection, Creation, Edge, LocalizedModel, Model, Modification} from "~/model/common";

type QueryResult<T extends LocalizedModel> = {models: Connection<T>};

type Mapper<S, T extends LocalizedModel> = (source: S) => T;

interface Options {
    refetch?: boolean,
    optimistic?: boolean,
    updateCacheOnSuccess?: boolean,
}

const defaultOptions = {
    refetch: false,
    optimistic: true,
    updateCacheOnSuccess: true,
};

function mergeOptions(options: Options): Required<Options> {
    return {
        ...defaultOptions,
        ...options,
    };
}

export function useModelStore<T extends LocalizedModel, M = Modification<T>, C = Creation<T>>(
    query: DocumentNode,
    createMutation: DocumentNode,
    updateMutation: DocumentNode,
    removeMutation: DocumentNode,
    fromModification: Mapper<S, T> | (M extends Modification<T> ? undefined : never),
    fromCreation: Mapper<S, T> | (C extends Creation<T> ? undefined : never),
)
export function useModelStore<T extends LocalizedModel, C = Creation<T>>(
    query: DocumentNode,
    createMutation: DocumentNode,
    updateMutation: DocumentNode,
    removeMutation: DocumentNode,
    fromModificationOrUndefined: Mapper<M, T> | undefined,
    fromCreationOrUndefined: Mapper<C, T> | undefined,
) {
    const fromModification: Mapper<M, T> = fromModificationOrUndefined ?? ((source) => ({
        locale: "en",
        ...source,
    }));
    const fromCreation: Mapper<C, T> = fromCreationOrUndefined ?? ((source) => ({
        locale: "en",
        id: crypto.randomUUID(),
        ...source,
    }));
    const {result, error, loading} = useAsyncQuery<QueryResult<T>>(query);

    function updateInResult(
        original: QueryResult<T>,
        model: T,
        id: string = model.id,
    ): QueryResult<T> {
        const mergeIfMatchingId = (edge: Edge<T>) => (edge.node.id != id) ? edge : {
            ...edge,
            node: model,
        };
        return {
            models: {
                edges: original.models.edges.map(mergeIfMatchingId),
                pageInfo: original.models.pageInfo,
            }
        };
    }

    function addToResult(
        original: QueryResult<T>,
        model: T,
    ): QueryResult<T> {
        return {
            models: {
                edges: [
                    ...original.models.edges,
                    {node: model},
                ],
                pageInfo: original.models.pageInfo,
            }
        };
    }

    const {mutate: createModel} = useMutation(createMutation);
    const {mutate: updateModel} = useMutation(updateMutation);
    const {mutate: removeModel} = useMutation(removeMutation);
    const models = computed(() => result.value?.models.edges.map(e => e.node) || []);

    function remove({id}: Model) {
        return removeModel({id}, {refetchQueries: [query]});
    }

    function add(creation: C, options: Options = {}) {
        const {
            refetch,
            optimistic,
            updateCacheOnSuccess,
        } = mergeOptions(options);
        let model: T | undefined;
        if (optimistic) {
            const {resolveClient} = useApolloClient()
            const client = resolveClient();
            const result = client.readQuery({query});
            model = fromCreation(creation);
            const data = addToResult(result, model);
            client.writeQuery({query, data, overwrite: true});
        }
        return createModel({creation: {...creation, locale: "en"}}, {
            update: !updateCacheOnSuccess ? undefined : (catche, result) => {
                // This assumes that all creation mutation returned value is aliased `created` in all queries
                const {created} = result.data;
                catche.updateQuery({query}, (data) => model ?
                    updateInResult(data, created, model.id) :
                    addToResult(data, created));
            },
            refetchQueries: !refetch ? undefined : [query],
        });
    }

    function edit(modification: M, options: Options = {}) {
        const {
            refetch,
            optimistic,
            updateCacheOnSuccess,
        } = mergeOptions(options);
        const model = fromModification(modification);
        if (optimistic) {
            const {resolveClient} = useApolloClient()
            const client = resolveClient();
            const result = client.readQuery({query});
            const data = updateInResult(result, model);
            client.writeQuery({query, data, overwrite: true});
        }
        return updateModel({modification}, {
            update: !updateCacheOnSuccess ? undefined : (catche, result) => {
                // This assumes that all update mutation returned value is aliased `updated` in all queries
                const {updated} = result.data;
                catche.updateQuery({query}, (data) => updateInResult(data, updated));
            },
            refetchQueries: !refetch ? undefined : [query],
        });
    }

    return {
        models,
        remove,
        add,
        edit,
        error,
        loading,
    };
}
