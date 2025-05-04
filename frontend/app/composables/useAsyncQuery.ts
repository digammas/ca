import type {DocumentNode} from "graphql/language";
import {useLazyQuery, useQuery, type UseQueryReturn} from "@vue/apollo-composable";
import {ApolloError} from "@apollo/client/core";

type UseAsyncQueryReturn<T, V extends Record<string, unknown>> = UseQueryReturn<T, V>;

export default function useAsyncQuery<T, V extends Record<string, unknown> = Record<string, unknown>>(
    key: string,
    document: DocumentNode,
    variables?: V
): UseAsyncQueryReturn<T, V | {}> {
    const {load} = useLazyQuery<T, V>(document, variables);
    const asyncData = useAsyncData<T, ApolloError>(key, () => load() || Promise.reject(new Error("Load called more tha once!")));
    if (import.meta.server) {
        return {
            result: asyncData.data as Ref<T>,
            loading: asyncData.pending,
            error: asyncData.error,
            networkStatus: ref(),
            refetch: () => undefined,
            start: () => undefined,
            restart: () => undefined,
            stop: () => undefined,
            fetchMore: () => undefined,
            subscribeToMore: () => undefined,
            updateQuery: () => undefined,
            onError: () => ({off() {}}),
            onResult: () => ({off() {}}),
            variables: ref(variables),
            options: ref({}),
            query: ref(),
            forceDisabled: ref(false),
            document: ref(document),
        }
    }
    // Fetch client-side to update Apollo cache
    const queryResult = useQuery<T, V | {}>(document, variables || {});
    return {
        ...queryResult,
        variables: queryResult.variables,
        result: computed(() => queryResult.result.value ?? asyncData.data.value as T),
        loading: computed(() => !!queryResult.result.value && queryResult.loading.value),
    };
}
