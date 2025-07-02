<script setup lang="ts">

import type {Creation, NamedModel} from "~/model/common";

type Emits = {
  add: [Creation<NamedModel>],
};

type Props = {
  placeholder: string,
};

const {placeholder} = defineProps<Props>();
const emit = defineEmits<Emits>();
const editing = ref(false);
const creationText = ref("");

function startCreation() {
  editing.value = true;
}

function confirmCreation() {
  if (creationText.value) {
    emit("add", {name: creationText.value});
  }
  resetValue();
}

function resetValue() {
  creationText.value = "";
}

function cancelCreation() {
  resetValue();
  editing.value = false;
}

</script>

<template>
  <div class="container" :class="{editing}" @focusout="cancelCreation">
    <input
        v-model="creationText"
        ref="input"
        :placeholder
        @keyup.esc="resetValue"
        @keyup.enter="confirmCreation"
        @focus="startCreation" />
    <template v-if="editing">
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

  input {
    font-size: 100%;
    border: none;
    padding: 0;
  }
}

</style>
