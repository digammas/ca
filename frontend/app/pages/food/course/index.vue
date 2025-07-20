<script setup lang="ts">
import gql from "graphql-tag";

const query = gql`
    query {
        models: courses {
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

const createMutation = gql`
    mutation ($creation: CourseCreation) {
        created: createCourse(creation: $creation) {
            id
            name
            locale
        }
    }
`;

const updateMutation = gql`
    mutation ($modification: CourseModification) {
        updated: updateCourse(modification: $modification) {
            id
            name
            locale
        }
    }
`;

const removeMutation = gql`
    mutation ($id: ID!) {
        deleted: deleteCourse(id: $id)
    }
`;

</script>

<template>
  <h2>Courses</h2>

  <AppNamedModelPage :query :createMutation :updateMutation :removeMutation
        loadingMessage="Loading courses..." errorMessage="Error fetching courses!" />

</template>

<style scoped>

</style>
