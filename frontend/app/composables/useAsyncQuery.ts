import type {DocumentNode} from "graphql/language";
import {useLazyQuery, useQuery, type UseQueryReturn} from "@vue/apollo-composable";
import {ApolloError} from "@apollo/client/core";

type UseAsyncQueryReturn<T, V extends Record<string, unknown>> = UseQueryReturn<T, V>;

function postFetch<T, V extends Record<string, unknown>>(
    document: DocumentNode,
    variables: V | undefined,
    asyncData: ReturnType<typeof useAsyncData<T | null, ApolloError>>,
): UseAsyncQueryReturn<T, V | {}> {
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

function useAsyncQueryWithKey<T, V extends Record<string, unknown> = Record<string, unknown>>(
    key: string,
    document: DocumentNode,
    variables?: V,
): UseAsyncQueryReturn<T, V | {}> {
    const {load} = useLazyQuery<T, V>(document, variables);
    const asyncData = useAsyncData<T, ApolloError>(key, () => load() || Promise.reject(new Error("Load called more tha once!")));
    return postFetch(document, variables, asyncData);
}

function useAsyncQueryWithoutKey<T, V extends Record<string, unknown> = Record<string, unknown>>(
    document: DocumentNode,
    variables?: V,
): UseAsyncQueryReturn<T, V | {}> {
    const {load} = useLazyQuery<T, V>(document, variables);
    const asyncData = useAsyncData<T, ApolloError>(() => load() || Promise.reject(new Error("Load called more tha once!")));
    return postFetch(document, variables, asyncData);
}

export default function useAsyncQuery<T, V extends Record<string, unknown> = Record<string, unknown>>(
    document: DocumentNode,
    variables?: V,
): UseAsyncQueryReturn<T, V | {}>;
export default function useAsyncQuery<T, V extends Record<string, unknown> = Record<string, unknown>>(
    key: string,
    document: DocumentNode,
    variables?: V,
): UseAsyncQueryReturn<T, V | {}>;
export default function useAsyncQuery<T, V extends Record<string, unknown> = Record<string, unknown>>(
    param1: string | DocumentNode,
    param2: DocumentNode | V | undefined,
    param3?: V | undefined,
): UseAsyncQueryReturn<T, V | {}> {
    if (typeof param1 === 'string') {
        return useAsyncQueryWithKey(param1, param2 as DocumentNode, param3);
    } else {
        return useAsyncQueryWithoutKey(param1, param2 as V | undefined);
    }
}
