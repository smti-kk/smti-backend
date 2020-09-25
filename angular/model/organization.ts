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
import { AccessPoint } from './accessPoint';
import { Location } from './location';
import { TypeOrganization } from './typeOrganization';
import { TypeSmo } from './typeSmo';


export interface Organization { 
    accessPoints?: Array<AccessPoint>;
    acronym?: string;
    address?: string;
    children?: Array<Organization>;
    fias?: string;
    id?: number;
    inn?: string;
    kpp?: string;
    location?: Location;
    name?: string;
    parent?: Organization;
    smo?: TypeSmo;
    type?: TypeOrganization;
}
