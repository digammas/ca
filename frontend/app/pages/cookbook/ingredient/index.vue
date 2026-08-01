<script setup lang="ts">
import gql from "graphql-tag";

const query = gql`
    query {
        models: ingredients {
            edges {
                node {
                    id
                    locale
                    name
                    description
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
    mutation ($creation: IngredientCreation) {
        created: createIngredient(creation: $creation) {
            id
            name
            description
            locale
        }
    }
`;

const updateMutation = gql`
    mutation ($modification: IngredientModification) {
        updated: updateIngredient(modification: $modification) {
            id
            name
            description
            locale
        }
    }
`;

const removeMutation = gql`
    mutation ($id: ID!) {
        deleted: deleteIngredient(id: $id)
    }
`;

</script>

<template>
  <h2>Ingredients</h2>

  <CookbookIngredientPage :query :createMutation :updateMutation :removeMutation
        loadingMessage="Loading ingredients..." errorMessage="Error fetching ingredients!" />

</template>

<style scoped>

</style>
