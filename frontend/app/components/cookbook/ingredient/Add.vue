<script setup lang="ts">

import type {Creation} from "~/model/common";
import type {Ingredient} from "~/model/cookbook";

type Emits = {
  add: [Creation<Ingredient>],
};

const emit = defineEmits<Emits>();
const editing = ref(false);
const nameText = ref("");
const descriptionText = ref("");
const container = ref<HTMLElement>();

function startCreation() {
  editing.value = true;
}

function confirmCreation() {
  if (nameText.value && descriptionText.value) {
    emit("add", {name: nameText.value, description: descriptionText.value});
  }
  resetValue();
}

function resetValue() {
  nameText.value = "";
  descriptionText.value = "";
}

function handleFocusOut(event: FocusEvent) {
  // Skip when focus moves within the widget itself (other field, confirm button)
  const relatedTarget = event.relatedTarget as Node | null;
  if (relatedTarget && container.value?.contains(relatedTarget)) return;
  resetValue();
  editing.value = false;
}

</script>

<template>
  <div class="container" :class="{editing}" ref="container" @focusout="handleFocusOut">
    <input
        v-model="nameText"
        placeholder="Name"
        @keyup.esc="resetValue"
        @keyup.enter="confirmCreation"
        @focus="startCreation" />
    <template v-if="editing">
      <input
          v-model="descriptionText"
          placeholder="Description"
          @keyup.esc="resetValue"
          @keyup.enter="confirmCreation" />
      <UiIconButton @click="confirmCreation" action="confirm" />
    </template>
  </div>
</template>

<style scoped>

.container {
  display: grid;
  grid-template-columns: 1fr 25px;
  max-width: 300px;
  height: 22px;

  &.editing {
    grid-template-columns: 1fr 1fr 25px;
    max-width: 500px;
  }

  input {
    font-size: 100%;
    border: none;
    padding: 0;
  }
}

</style>
