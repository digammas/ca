<script setup lang="ts">
import type {Model, Modification} from "~/model/common";
import type {Ingredient} from "~/model/cookbook";

type Props = {
  item: Ingredient,
};

type Emits = {
  edit: [model: Modification<Ingredient>],
  remove: [model: Model],
};

const {item} = defineProps<Props>();
const emit = defineEmits<Emits>();
const editing = ref(false);
const editingName = ref<string>(item.name);
const editingDescription = ref<string>(item.description);
const nameInput = ref<HTMLInputElement>();
const container = ref<HTMLElement>();

function startEditing() {
  editing.value = true;
  nextTick().then(() => nameInput?.value?.focus())
}

function confirmEditing() {
  if (editingName.value && editingDescription.value) {
    emit("edit", {
      id: item.id,
      name: editingName.value,
      description: editingDescription.value,
    });
  }
  editing.value = false;
}

function handleFocusOut(event: FocusEvent) {
  // Skip when focus moves within the widget itself (other field, confirm button)
  const relatedTarget = event.relatedTarget as Node | null;
  if (relatedTarget && container.value?.contains(relatedTarget)) return;
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
    <span @click="startEditing" class="editable">{{ item.description }}</span>
    <client-only>
      <UiIconButton action="cancel" @click="remove" />
    </client-only>
  </div>
  <div v-else class="container editing" ref="container" @focusout="handleFocusOut">
    <input v-model="editingName" ref="nameInput"
           @keyup.esc="handleEscape" @keyup.enter="confirmEditing" />
    <input v-model="editingDescription"
           @keyup.esc="handleEscape" @keyup.enter="confirmEditing" />
    <UiIconButton action="confirm" @click="confirmEditing" />
  </div>
</template>

<style scoped>

.container {
  display: grid;
  grid-template-columns: 1fr 1fr 25px;
  max-width: 500px;
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
