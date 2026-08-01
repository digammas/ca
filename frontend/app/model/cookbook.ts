import type {NamedModel} from "~/model/common";

export interface Ingredient extends NamedModel {
    readonly description: string;
}
