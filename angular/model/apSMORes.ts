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
import { AccessPointRes } from './accessPointRes';
import { GovernmentDevelopmentProgram } from './governmentDevelopmentProgram';
import { Operator } from './operator';
import { PointRes } from './pointRes';
import { TypeInternetAccess } from './typeInternetAccess';


export interface ApSMORes extends AccessPointRes { 
    address?: string;
    billingId?: number;
    completed?: number;
    connectionState?: ApSMORes.ConnectionStateEnum;
    contractor?: string;
    createdBy?: string;
    createdDate?: Date;
    customer?: string;
    declaredSpeed?: string;
    deleted?: boolean;
    description?: string;
    governmentDevelopmentProgram?: GovernmentDevelopmentProgram;
    id?: number;
    internetAccess?: TypeInternetAccess;
    ipConfig?: string;
    maxAmount?: number;
    modifiedBy?: string;
    modifiedDate?: Date;
    networks?: string;
    operator?: Operator;
    point?: PointRes;
    quality?: string;
    state?: string;
    ucn?: number;
    visible?: boolean;
}
export namespace ApSMORes {
    export type ConnectionStateEnum = 'ACTIVE' | 'DISABLED' | 'NOT_MONITORED';
    export const ConnectionStateEnum = {
        ACTIVE: 'ACTIVE' as ConnectionStateEnum,
        DISABLED: 'DISABLED' as ConnectionStateEnum,
        NOTMONITORED: 'NOT_MONITORED' as ConnectionStateEnum
    };
}
