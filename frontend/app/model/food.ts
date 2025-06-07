import type {Connection, NamedModel} from "~/model/common";

export interface Cuisine extends NamedModel {}

export type Cuisines = Connection<Cuisine>;
