import {ApolloClient, InMemoryCache} from "@apollo/client/core";
import {DefaultApolloClient} from "@vue/apollo-composable";


export default defineNuxtPlugin((nuxtApp) => {
    const apolloClient = new ApolloClient({
        cache: new InMemoryCache(),
        uri: "http://localhost:8082/graphql",
    });
    nuxtApp.vueApp.provide(DefaultApolloClient, apolloClient);
});
