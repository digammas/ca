import {ApolloClient, HttpLink, InMemoryCache} from "@apollo/client/core";
import {DefaultApolloClient} from "@vue/apollo-composable";

type FetchRequestParams = Parameters<typeof fetch>[1];

type $Fetch = typeof $fetch;

type $ExtendedFetch = $Fetch & {
    native: $Fetch,
}

function customFetch(_: unknown, params: FetchRequestParams): Promise<Response> {
    return ($fetch as $ExtendedFetch).native("/graphql", {
        body: params?.body,
        headers: params?.headers,
        method: "POST",
    });
}

export default defineNuxtPlugin((nuxtApp) => {
    const apolloClient = new ApolloClient({
        cache: new InMemoryCache(),
        link: new HttpLink({
            fetch: customFetch,
        }),
    });
    nuxtApp.vueApp.provide(DefaultApolloClient, apolloClient);
});
