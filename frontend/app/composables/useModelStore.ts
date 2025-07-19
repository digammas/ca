import type {DocumentNode} from "graphql/language";
import {useMutation} from "@vue/apollo-composable";
import type {Connection, Creation, LocalizedModel, Model} from "~/model/common";

export function useModelStore<T extends LocalizedModel, C = Creation<T>>(
    query: DocumentNode,
    createMutation: DocumentNode,
    updateMutation: DocumentNode,
    removeMutation: DocumentNode,
) {
    const {result, error, loading} = useAsyncQuery<{models: Connection<T>}>(query);
    const mutationParams = {refetchQueries: [query]};
    const {mutate: createModel} = useMutation(createMutation, mutationParams);
    const {mutate: updateModel} = useMutation(updateMutation, mutationParams);
    const {mutate: removeModel} = useMutation(removeMutation, mutationParams);
    const models = computed(() => result.value?.models.edges.map(e => e.node) || []);

    function remove({id}: Model) {
        return removeModel({id});
    }

    function add(creation: C) {
        return createModel({creation: {...creation, locale: "en"}});
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
