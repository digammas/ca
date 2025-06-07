<script setup lang="ts">
import gql from "graphql-tag";

const query = gql`
    query {
        models: cuisines {
            edges {
                node {
                    id
                    locale
                    name
                }
            }
            pageInfo {
                hasNextPage
                endCursor
            }
        }
    }
`;

const createMutation = gql`
    mutation ($creation: CuisineCreation) {
        createCuisine(creation: $creation) {
            id
            name
            locale
        }
    }
`;

const updateMutation = gql`
    mutation ($modification: CuisineModification) {
        updateCuisine(modification: $modification) {
            id
            name
            locale
        }
    }
`;

const removeMutation = gql`
    mutation ($id: ID!) {
        deleteCuisine(id: $id)
    }
`;


</script>

<template>
  <h2>Cuisines</h2>

  <AppNamedModelPage :query :createMutation :updateMutation :removeMutation
        loadingMessage="Loading cuisines..." errorMessage="Error fetching cuisines!" />
</template>

<style scoped>

</style>
