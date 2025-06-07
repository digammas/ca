<script setup lang="ts">
import {useMutation} from "@vue/apollo-composable";
import type {DocumentNode} from "graphql/language";
import type {Connection, NamedModel} from "~/model/common";

type Props = {
  query: DocumentNode,
  createMutation: DocumentNode,
  updateMutation: DocumentNode,
  removeMutation: DocumentNode,
  loadingMessage: string,
  errorMessage: string,
}
const {
  query,
  createMutation,
  updateMutation,
  removeMutation,
} = defineProps<Props>();

const {result, error, loading, refetch} = useAsyncQuery<{models: Connection<NamedModel>}>(query);
const {mutate: createModel} = useMutation(createMutation);
const {mutate: updateModel} = useMutation(updateMutation);
const {mutate: removeModel} = useMutation(removeMutation);
const models = computed(() => result.value?.models.edges.map(e => e.node) || []);

function remove({id}: NamedModel) {
  removeModel( {id})
      // refetch needed since deletion doesn't update cache automatically
      .then(() => refetch());
}

function add(creation: {name: string}) {
  createModel( {creation: {...creation, locale: "en"}})
      // refetch needed since creation doesn't update cache automatically
      .then(() => refetch());
}

function edit(model: NamedModel) {
  updateModel( {modification: model});
}

</script>

<template>
  <h2>Cuisines</h2>

  <AppNamedModelList
      :items="models" :error="error" :loading="loading" creation-placeholder="New cuisine"
      @remove="remove" @edit="edit" @add="add">
    <template #loading>{{ loadingMessage }}</template>
    <template #error>{{ errorMessage }}</template>
  </AppNamedModelList>

</template>

<style scoped>

</style>
