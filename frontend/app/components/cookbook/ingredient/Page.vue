<script setup lang="ts">
import type {DocumentNode} from "graphql/language";
import {useModelStore} from "~/composables/useModelStore";
import type {Ingredient} from "~/model/cookbook";

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
} = useModelStore<Ingredient>(
    query,
    createMutation,
    updateMutation,
    removeMutation,
);

</script>

<template>
  <AppModelList :items :error :loading>
    <template #item="item">
      <CookbookIngredientItem :item @remove="remove" @edit="edit" />
    </template>
    <template #addItem>
      <CookbookIngredientAdd @add="add" />
    </template>
    <template #loading>{{ loadingMessage }}</template>
    <template #error>{{ errorMessage }}</template>
  </AppModelList>
</template>

<style scoped>

</style>
