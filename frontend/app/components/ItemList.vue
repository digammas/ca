<script setup lang="ts" generic="T extends Model">
import type {Model} from "~/model/common";

type Props<T> = {
  items: T[];
  error?: Error | null;
  loading?: boolean;
  ordered?: boolean;
}

const {ordered} = defineProps<Props<T>>()

const listTag = ordered ? "ol" : "ul";

</script>

<template>

  <div v-if="loading">
    <slot name="loading">Loading...</slot>
  </div>
  <div v-else-if="error">
    <slot name="error">{{ error.message }}</slot>
  </div>
  <div v-else>
    <slot name="header"></slot>
    <component :is="listTag" v-if="items.length">
      <li v-for="item in items" :key="item.id">
        <slot name="item" v-bind="item">
          {{ item }}
        </slot>
      </li>
    </component>
    <p v-else>No cuisine has been created yet.</p>
  </div>

</template>

<style scoped>

</style>
