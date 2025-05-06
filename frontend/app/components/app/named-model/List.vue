<script setup lang="ts">
import type {NamedModel} from "~/model/common";

type Props = {
  items: NamedModel[],
  error?: Error | null,
  loading?: boolean,
  ordered?: boolean,
  creationPlaceholder?: string,
};

type Emits = {
  edit: [model: NamedModel],
  remove: [model: NamedModel],
  add: [{name: string}],
};

const {ordered, creationPlaceholder} = defineProps<Props>();
const emit = defineEmits<Emits>()
const listTag = ordered ? "ol" : "ul";
const add = (model: {name: string}) => emit("add", model);
const remove = (model: NamedModel) => emit("remove", model);
const edit = (model: NamedModel) => emit("edit", model);
const addPlaceholder = creationPlaceholder || "New item";

</script>

<template>
  <div v-if="loading">
    <slot name="loading">Loading...</slot>
  </div>
  <div v-else-if="error">
    <slot name="error">{{ error.message }}</slot>
  </div>
  <div>
    <slot name="header"></slot>
    <component :is="listTag">
      <li v-for="item in items" :key="item.id">
        <AppNamedModelItem :item @remove="remove" @edit="edit" />
      </li>
      <li>
        <AppNamedModelAdd :placeholder="addPlaceholder" @add="add" />
      </li>
    </component>
  </div>
</template>

<style scoped>

</style>
