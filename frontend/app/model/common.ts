export interface Model {
    readonly id: string;
}

export interface LocalizedModel extends Model {
    readonly locale: string;
}

export interface NamedModel extends LocalizedModel {
    readonly name: string;
}

export interface PageInfo {
    readonly startCursor?: string;
    readonly endCursor: string;
    readonly hasPreviousPage?: boolean;
    readonly hasNextPage: boolean;
}

export interface Edge<M extends Model> {
    readonly node: M;
    readonly cursor: string;
}

export interface Connection<M extends Model> {
    readonly edges: readonly Edge<M>[];
    readonly pageInfo: PageInfo;
}

export type Creation<T extends LocalizedModel> = Omit<T, keyof LocalizedModel>
