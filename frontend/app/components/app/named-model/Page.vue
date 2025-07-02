<script setup lang="ts">
import type {DocumentNode} from "graphql/language";
import {useModelStore} from "~/composables/useModelStore";

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

const {
  models: items,
  remove,
  edit,
  add,
  error,
  loading,

} = useModelStore(
    query,
    createMutation,
    updateMutation,
    removeMutation,
);

</script>

<template>
  <AppModelList :items :error :loading
      @remove="remove" @edit="edit">
    <template #item="item">
      <AppNamedModelItem :item  @remove="remove" @edit="edit" />
    </template>
    <template #addItem="creationPlaceholder">
      <AppNamedModelAdd placeholder="New Item" @add="add" />
    </template>
    <template #loading>{{ loadingMessage }}</template>
    <template #error>{{ errorMessage }}</template>
  </AppModelList>
</template>

<style scoped>

</style>
