<script setup lang="ts">

type Props = {
  action: "confirm" | "cancel" | "edit",
};

const {action} = defineProps<Props>();

const texts: Record<Props["action"], string> = {
  confirm: "✔",
  cancel: "🗙",
  edit: "✎",
};

const text = texts[action];

</script>

<template>
  <button>{{ text }}</button>
</template>

<style scoped>
button {
  background:transparent;
  border:none;
  cursor: pointer;
}
</style>
