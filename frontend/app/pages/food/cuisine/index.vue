<script setup lang="ts">
import gql from "graphql-tag";
import type {Cuisine, Cuisines} from "~/model/food";
import {useMutation} from "@vue/apollo-composable";

const query = gql`
    query {
        cuisines {
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

const {result, error, loading, refetch} = useAsyncQuery<{cuisines: Cuisines}>("cuisines", query);
const {mutate: createCuisine} = useMutation(createMutation);
const {mutate: updateCuisine} = useMutation(updateMutation);
const {mutate: removeCuisine} = useMutation(removeMutation);
const cuisines = computed(() => result.value?.cuisines.edges.map(e => e.node) || []);

function remove({id}: Cuisine) {
  removeCuisine( {id})
      // refetch needed since deletion doesn't update cache automatically
      .then(() => refetch());
}

function add(creation: {name: string}) {
  createCuisine( {creation: {...creation, locale: "en"}})
      // refetch needed since creation doesn't update cache automatically
      .then(() => refetch());
}

function edit(cuisine: Cuisine) {
  const modification = {
    id: cuisine.id,
    name: cuisine.name,
  };
  updateCuisine( {modification});
}

</script>

<template>
  <h2>Cuisines</h2>

  <AppNamedModelList
      :items="cuisines" :error="error" :loading="loading" creation-placeholder="New cuisine"
      @remove="remove" @edit="edit" @add="add">
    <template #loading>Loading cuisines...</template>
    <template #error>Error fetching cuisines!</template>
  </AppNamedModelList>

</template>

<style scoped>

</style>
