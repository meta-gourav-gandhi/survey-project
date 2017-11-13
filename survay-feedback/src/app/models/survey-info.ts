import { SurveyStatus } from './survey-status';
export class SurveyInfo {
    id : number;
    surveyName : string;
    description : string;
    status : SurveyStatus;
    surveyUrl : string;
    labels : string;
}