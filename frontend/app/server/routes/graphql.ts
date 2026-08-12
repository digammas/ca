export default defineEventHandler(async (event): Promise<unknown> => {
    const body = await readBody(event);
    const method = "POST";
    return await $fetch<unknown>("http://localhost:8080/graphql", {method, body});
});
