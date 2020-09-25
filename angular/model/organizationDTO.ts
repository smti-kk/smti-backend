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
import { LocationDTO } from './locationDTO';
import { TypeOrgDTO } from './typeOrgDTO';
import { TypeSmoDTO } from './typeSmoDTO';


export interface OrganizationDTO { 
    acronym?: string;
    address?: string;
    fias?: string;
    id?: number;
    inn?: string;
    kpp?: string;
    location?: LocationDTO;
    name?: string;
    parent?: number;
    smo?: TypeSmoDTO;
    type?: TypeOrgDTO;
}
