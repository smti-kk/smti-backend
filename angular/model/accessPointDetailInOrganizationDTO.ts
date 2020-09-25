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
import { GovernmentDevelopmentProgram } from './governmentDevelopmentProgram';
import { OperatorSimpleDTO } from './operatorSimpleDTO';
import { PointRes } from './pointRes';
import { TypeInternetAccessDTO } from './typeInternetAccessDTO';


export interface AccessPointDetailInOrganizationDTO { 
    address?: string;
    amount?: number;
    billingId?: number;
    completed?: number;
    contractor?: string;
    createdAt?: Date;
    customer?: string;
    declaredSpeed?: string;
    description?: string;
    ended?: string;
    governmentProgram?: GovernmentDevelopmentProgram;
    hardware?: string;
    id?: number;
    internetAccess?: TypeInternetAccessDTO;
    ipConfig?: string;
    maxAmount?: number;
    name?: string;
    netTrafficLastMonth?: number;
    netTrafficLastWeek?: number;
    networks?: string;
    number?: string;
    operator?: OperatorSimpleDTO;
    point?: PointRes;
    quality?: string;
    software?: string;
    started?: string;
    state?: string;
    type?: string;
    ucn?: number;
    updatedAt?: Date;
    visible?: boolean;
}
