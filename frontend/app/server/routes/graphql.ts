export default defineEventHandler(async event => {
    const body = await readBody(event);
    const method = "POST";
    return await event.$fetch("http://localhost:8080/graphql", {method, body});
});
