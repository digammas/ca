<script setup lang="ts">
import gql from "graphql-tag";

const query = gql`
    query {
        models: servings {
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
    mutation ($creation: ServingCreation) {
        createServing(creation: $creation) {
            id
            name
            locale
        }
    }
`;

const updateMutation = gql`
    mutation ($modification: ServingModification) {
        updateServing(modification: $modification) {
            id
            name
            locale
        }
    }
`;

const removeMutation = gql`
    mutation ($id: ID!) {
        deleteServing(id: $id)
    }
`;


</script>

<template>
  <h2>Servings</h2>

  <AppNamedModelPage :query :createMutation :updateMutation :removeMutation
                     loadingMessage="Loading servings..." errorMessage="Error fetching servings!" />

</template>

<style scoped>

</style>
