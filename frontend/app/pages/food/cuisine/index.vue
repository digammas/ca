<script setup lang="ts">
import gql from "graphql-tag";

import type {Cuisines} from "~/model/food";
import {useQuery} from "@vue/apollo-composable";


const query = gql`
    query {
        cuisines {
            edges {
                node {
                    id
                    locale
                    name
                }
            }
            pageInfo {
                hasNextPage
                endCursor
            }
        }
    }
`;

const { result } = useQuery<Cuisines>(query);
const cuisines = result.value?.edges || [];

</script>

<template>
<h2>Cuisines</h2>
<ul v-if="cuisines.length">
  <li v-for="cuisine in cuisines" :key="cuisine.id">{{ cuisine.name }}</li>
</ul>
<p v-else>No cuisine has been created yet.</p>
</template>

<style scoped>

</style>
