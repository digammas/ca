import type {Connection, LocalizedModel} from "~/model/common";

export interface Cuisine extends LocalizedModel {
    name: string;
}

export type Cuisines = Connection<Cuisine>;
