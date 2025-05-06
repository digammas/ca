<script setup lang="ts">
import type {NamedModel} from "~/model/common";

type Props = {
  item: NamedModel,
};

type Emits = {
  edit: [model: NamedModel],
  remove: [model: NamedModel],
};

const {item} = defineProps<Props>();
const emit = defineEmits<Emits>();
const editing = ref(false);
const editingText = ref<string>(item.name);


function startEditing() {
  editing.value = true;
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
  <div class="container" :class="{editing}" @focusout="cancelEditing" >
    <template v-if="!editing">
      <span @click="startEditing" class="editable">{{ item.name }}</span>
      <UiIconButton @click="remove" action="cancel" />
    </template>
    <template v-else>
      <input v-model="editingText" autofocus ref="input" @keyup.esc="cancelEditing" @keyup.enter="confirmEditing" />
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
