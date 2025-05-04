import type {DocumentNode} from "graphql/language";
import {useLazyQuery} from "@vue/apollo-composable";


export default function useAsyncQuery<T>(key: string, document: DocumentNode) {
    const {load} = useLazyQuery<T>(document);
    return  useAsyncData<T | undefined>(key, () => load() || Promise.resolve(undefined));
}
