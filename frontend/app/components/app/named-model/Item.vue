<script setup lang="ts">
import type {Model, Modification, NamedModel} from "~/model/common";

type Props = {
  item: NamedModel,
};

type Emits = {
  edit: [model: Modification<NamedModel>],
  remove: [model: Model],
};

const {item} = defineProps<Props>();
const emit = defineEmits<Emits>();
const editing = ref(false);
const editingText = ref<string>(item.name);
const input = ref<HTMLInputElement>();
const confirmButton = ref<UiIconButton>();

function startEditing() {
  editing.value = true;
  nextTick().then(() => input?.value?.focus())
}

function confirmEditing() {
  if (editingText.value) {
    emit("edit", {
      id: item.id,
      name: editingText.value,
    });
  }
  editing.value = false;
}

async function handleFocusOut(event: FocusEvent) {
  // Skip when confirm button is pressed
  if (event.relatedTarget === confirmButton.value?.$el) return;
  editing.value = false;
}

function handleEscape() {
  editing.value = false;
}

function remove() {
  emit('remove', item);
}

</script>

<template>
  <div v-if="!editing" class="container">
    <span @click="startEditing" class="editable">{{ item.name }}</span>
    <client-only>
      <UiIconButton action="cancel" @click="remove" />
    </client-only>
  </div>
  <div v-else class="container editing" @focusout="handleFocusOut">
    <input v-model="editingText" ref="input"
           @keyup.esc="handleEscape" @keyup.enter="confirmEditing" />
    <UiIconButton ref="confirmButton" action="confirm" @click="confirmEditing" />
  </div>
</template>

<style scoped>

.container {
  display: grid;
  grid-template-columns: 1fr 25px;
  max-width: 300px;
  height: 22px;

  .editable {
    font-family: sans-serif;
  }

  input {
    font-size: 100%;
    border: none;
    padding: 0;
  }
}

</style>
