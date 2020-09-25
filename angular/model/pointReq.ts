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
import { CoordinateSequenceReq } from './coordinateSequenceReq';
import { EnvelopeReq } from './envelopeReq';
import { GeometryFactoryReq } from './geometryFactoryReq';


export interface PointReq { 
    coordinates?: CoordinateSequenceReq;
    envelope?: EnvelopeReq;
    factory?: GeometryFactoryReq;
    srid?: number;
    userData?: any;
}
