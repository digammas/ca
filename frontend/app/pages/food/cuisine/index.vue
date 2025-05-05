<script setup lang="ts">
import gql from "graphql-tag";
import type {Cuisines} from "~/model/food";
import {useMutation, useQuery} from "@vue/apollo-composable";

const cuisineCreation = ref({
  name: "",
  locale: "en",
});

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

const removeMutation = gql`
    mutation ($id: ID!) {
        deleteCuisine(id: $id)
    }
`;
const {result, error, loading, refetch} = useAsyncQuery<{cuisines: Cuisines}>("cuisines", query);
const {mutate: createCuisine} = useMutation(createMutation);
const {mutate: removeCuisine} = useMutation(removeMutation);
const cuisines = computed(() => result.value?.cuisines.edges.map(e => e.node) || []);

function create() {
  createCuisine( {creation: cuisineCreation.value})
      // refetch needed since creation doesn't update cache automatically
      .then(() => refetch());
}

function remove(id: string) {
  removeCuisine( {id})
      // refetch needed since deletion doesn't update cache automatically
      .then(() => refetch());
}

</script>

<template>
  <h2>Cuisines</h2>
  <AppItemList :items="cuisines" :error="error" :loading="loading">
    <template #loading>Loading cuisines...</template>
    <template #error>Error fetching cuisines!</template>
    <template #header><h3>Available cuisines</h3></template>
    <template #item="cuisine">
      <span>{{ cuisine.name }}</span>&nbsp;
      <a href="#" @click.stop="remove(cuisine.id)">remove</a>
    </template>
  </AppItemList>

  <h3>Create a new cuisine</h3>
  <form>
    <label for="cuisine-name-input">Cuisine name</label>
    <input id="cuisine-name-input" v-model="cuisineCreation.name" @keyup.enter="create"/>
    <button type="button" @click="create">Create</button>
  </form>

</template>

<style scoped>

</style>
