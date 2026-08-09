import type {DocumentNode} from "graphql/language";
import {useApolloClient, useMutation} from "@vue/apollo-composable";
import type {Connection, Creation, Edge, LocalizedModel, Model, Modification} from "~/model/common";
import type {ApolloClient, Unmasked} from "@apollo/client/core";

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

function defaultFromModification<T, M>(source: M): T {
    return {
        locale: "en",
        ...source,
    } as T;
}

function defaultFromCreation<T, C>(source: C): T {
    return {
        locale: "en",
        id: crypto.randomUUID(),
        ...source,
    } as T;
}

/**
 * Write a query, asserting that data is unmasked.
 */
function writeQuery<T>(
    client: ApolloClient<unknown>,
    query: DocumentNode,
    data: T,
) {
    client.writeQuery({query, data: data as Unmasked<T>, overwrite: true});
}

export function useModelStore<T extends LocalizedModel, M = Modification<T>, C = Creation<T>>(
    query: DocumentNode,
    createMutation: DocumentNode,
    updateMutation: DocumentNode,
    removeMutation: DocumentNode,
    fromModificationOrUndefined: Mapper<M, T> | (M extends Modification<T> ? undefined : never),
    fromCreationOrUndefined: Mapper<C, T> | (C extends Creation<T> ? undefined : never),
) {
    const fromModification: Mapper<M, T> = fromModificationOrUndefined ?? defaultFromModification;
    const fromCreation: Mapper<C, T> = fromCreationOrUndefined ?? defaultFromCreation;
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
        const cursor = String(original.models.edges.length);
        return {
            models: {
                edges: [
                    ...original.models.edges,
                    {
                        node: model,
                        cursor,
                    },
                ],
                pageInfo: {
                    ...original.models.pageInfo,
                    endCursor: cursor,
                },
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
            writeQuery(client, query, data);
        }
        return createModel({creation: {...creation, locale: "en"}}, {
            update: !updateCacheOnSuccess ? undefined : (cache, result) => {
                // This assumes that all creation mutation returned value is aliased `created` in all queries
                const {created} = result.data;
                cache.updateQuery({query}, (data) => model ?
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
            writeQuery(client, query, data);
        }
        return updateModel({modification}, {
            update: !updateCacheOnSuccess ? undefined : (cache, result) => {
                // This assumes that all update mutation returned value is aliased `updated` in all queries
                const {updated} = result.data;
                cache.updateQuery({query}, (data) => updateInResult(data, updated));
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
