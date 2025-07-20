import type {DocumentNode} from "graphql/language";
import {useApolloClient, useMutation} from "@vue/apollo-composable";
import type {Connection, Creation, Edge, LocalizedModel, Model} from "~/model/common";

export function useModelStore<T extends LocalizedModel, C = Creation<T>>(
    query: DocumentNode,
    createMutation: DocumentNode,
    updateMutation: DocumentNode,
    removeMutation: DocumentNode,
) {
    const {result, error, loading} = useAsyncQuery<{models: Connection<T>}>(query);
    const mutationParams = {refetchQueries: [query]};
    const {mutate: createModel} = useMutation(createMutation, mutationParams);

    interface Options {
        refetch?: boolean,
        optimistic?: boolean,
        updateCacheOnSuccess?: boolean,
    }

    const defaultOptions = {
        refetch: false,
        optimistic: true,
        updateCacheOnSuccess: true,
    }

    function mergeOptions(options: Options): Required<Options> {
        return {
            ...defaultOptions,
            ...options,
        }
    }

    function updateModelsAccordingToReference(
        original: {models: Connection<T>},
        reference: T,
        ): {models: Connection<T>} {
        const updateMatchingById = (edge: Edge<T>) => (edge.node.id != reference.id) ? edge : {
            ...edge,
            node: {
                ...edge.node,
                ...reference,
            },
        }
        return {
            models: {
                edges: original.models.edges.map(updateMatchingById),
                pageInfo: original.models.pageInfo,
            }
        }
    }

    const {mutate: updateModel} = useMutation(updateMutation, {
        update(catche, result) {
            const updated = result.data.updated;
            catche.updateQuery({query}, (data) => updateModelsAccordingToReference(data, updated));
        }
    });
    const {mutate: removeModel} = useMutation(removeMutation, mutationParams);
    const models = computed(() => result.value?.models.edges.map(e => e.node) || []);

    function remove({id}: Model) {
        return removeModel({id});
    }

    function add(creation: C) {
        return createModel({creation: {...creation, locale: "en"}});
    }

    function edit(model: T, options: Options = {}) {
        const {
            refetch,
            optimistic,
            updateCacheOnSuccess,
        } = mergeOptions(options);
        if (optimistic) {
            const {resolveClient} = useApolloClient()
            const client = resolveClient();
            const result = client.readQuery({query});
            const data = updateModelsAccordingToReference(result, model);
            client.writeQuery({query, data, overwrite: true});
        }
        return updateModel({modification: model}, {
            update: !updateCacheOnSuccess ? undefined :  (catche, result) => {
                const updated = result.data.updated;
                catche.updateQuery({query}, (data) => updateModelsAccordingToReference(data, updated));
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
    }
}
