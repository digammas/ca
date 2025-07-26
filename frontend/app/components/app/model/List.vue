<script setup lang="ts" generic="T extends LocalizedModel">
import type {LocalizedModel} from "~/model/common";

type Props = {
  items: T[],
  error?: Error | null,
  loading?: boolean,
  ordered?: boolean,
};

const {ordered} = defineProps<Props>();
const listTag = ordered ? "ol" : "ul";

</script>

<template>
  <div v-if="loading">
    <slot name="loading">Loading...</slot>
  </div>
  <div v-else-if="error">
    <slot name="error">{{ error.message }}</slot>
  </div>
  <div>
    <component :is="listTag">
      <li v-for="item in items" :key="item.id">
        <slot name="item" v-bind="item"></slot>
      </li>
      <client-only>
        <li>
          <slot name="addItem"></slot>
        </li>
      </client-only>
    </component>
  </div>
</template>

<style scoped>

</style>
