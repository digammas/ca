<script setup lang="ts" generic="T extends LocalizedModel, C">
import {useMutation} from "@vue/apollo-composable";
import type {DocumentNode} from "graphql/language";
import type {Connection, LocalizedModel, Model} from "~/model/common";

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

const {result, error, loading, refetch} = useAsyncQuery<{models: Connection<T>}>(query);
const {mutate: createModel} = useMutation(createMutation);
const {mutate: updateModel} = useMutation(updateMutation);
const {mutate: removeModel} = useMutation(removeMutation);
const models = computed(() => result.value?.models.edges.map(e => e.node) || []);

function remove({id}: Model) {
  removeModel( {id})
      // refetch needed since deletion doesn't update cache automatically
      .then(() => refetch());
}

function add(creation: C) {
  createModel( {creation: {...creation, locale: "en"}})
      // refetch needed since creation doesn't update cache automatically
      .then(() => refetch());
}

function edit(model: T) {
  updateModel( {modification: model});
}

</script>

<template>
  <AppModelList
      :items="models" :error="error" :loading="loading"
      @remove="remove" @edit="edit" @add="add">
    <template #item="item">
      <slot name="item" v-bind="item"></slot>
    </template>
    <template #addItem="creationPlaceholder">
      <slot name="addItem" v-bind="creationPlaceholder" @add="add"></slot>
    </template>
    <template #loading>{{ loadingMessage }}</template>
    <template #error>{{ errorMessage }}</template>
  </AppModelList>
</template>

<style scoped>

</style>
