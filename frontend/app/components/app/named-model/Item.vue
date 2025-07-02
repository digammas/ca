<script setup lang="ts">
import type {Model, NamedModel} from "~/model/common";

type Props = {
  item: NamedModel,
};

type Emits = {
  edit: [model: NamedModel],
  remove: [model: Model],
};

const {item} = defineProps<Props>();
const emit = defineEmits<Emits>();
const editing = ref(false);
const editingText = ref<string>(item.name);
const input = ref<HTMLInputElement>();

function startEditing() {
  editing.value = true;
  nextTick().then(() => input?.value?.focus())
}

function confirmEditing() {
  if (editingText.value) {
    emit("edit", {
      ...item,
      name: editingText.value,
    });
  }
  editing.value = false;
}

function cancelEditing() {
  editingText.value = item.name;
  editing.value = false;
}

function remove() {
  emit('remove', item);
}

</script>

<template>
  <div class="container" :class="{editing}" >
    <template v-if="!editing">
      <span @click="startEditing" class="editable">{{ item.name }}</span>
      <UiIconButton @click="remove" action="cancel" />
    </template>
    <template v-else>
      <input v-model="editingText" ref="input"
             @keyup.esc="cancelEditing" @keyup.enter="confirmEditing" @focusout="cancelEditing" />
      <UiIconButton @click="confirmEditing" action="confirm" />
    </template>
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
