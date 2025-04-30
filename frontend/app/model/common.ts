export interface Model {
    readonly id: string;
}

export interface LocalizedModel extends Model {
    readonly local: string;
}

export interface PageInfo {
    readonly startCursor?: string;
    readonly endCursor: string;
    readonly hasPreviousPage?: boolean;
    readonly hasNextPage: boolean;
}

export interface Connection<M extends Model> {
    readonly edges: readonly M[];
    readonly pageInfo: PageInfo;
}
