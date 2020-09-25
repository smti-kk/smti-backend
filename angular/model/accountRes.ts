/**
 * Api Documentation
 * Api Documentation
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { DLocationBase } from './dLocationBase';
import { OrganizationRes } from './organizationRes';


export interface AccountRes { 
    email?: string;
    firstName?: string;
    id?: number;
    isActive?: boolean;
    lastName?: string;
    locations?: Array<DLocationBase>;
    organizations?: Array<OrganizationRes>;
    patronymicName?: string;
    roles?: Array<AccountRes.RolesEnum>;
    username?: string;
}
export namespace AccountRes {
    export type RolesEnum = 'ADMIN' | 'GUEST' | 'MUNICIPALITY' | 'OPERATOR' | 'ORGANIZATION';
    export const RolesEnum = {
        ADMIN: 'ADMIN' as RolesEnum,
        GUEST: 'GUEST' as RolesEnum,
        MUNICIPALITY: 'MUNICIPALITY' as RolesEnum,
        OPERATOR: 'OPERATOR' as RolesEnum,
        ORGANIZATION: 'ORGANIZATION' as RolesEnum
    };
}
