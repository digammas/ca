import type {DocumentNode} from "graphql/language";
import {useMutation} from "@vue/apollo-composable";
import type {Connection, Creation, LocalizedModel, Model} from "~/model/common";

export function useModelStore<T extends LocalizedModel, C = Creation<T>>(
    query: DocumentNode,
    createMutation: DocumentNode,
    updateMutation: DocumentNode,
    removeMutation: DocumentNode,
) {
    const {result, error, loading, refetch} = useAsyncQuery<{models: Connection<T>}>(query);
    const {mutate: createModel} = useMutation(createMutation);
    const {mutate: updateModel} = useMutation(updateMutation);
    const {mutate: removeModel} = useMutation(removeMutation);
    const models = computed(() => result.value?.models.edges.map(e => e.node) || []);

    function remove({id}: Model) {
        return removeModel({id})
            // refetch needed since deletion doesn't update cache automatically
            .then(() => refetch());
    }

    function add(creation: C) {
        return createModel({creation: {...creation, locale: "en"}})
            // refetch needed since creation doesn't update cache automatically
            .then(() => refetch());
    }

    function edit(model: T) {
        return updateModel({modification: model});
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
