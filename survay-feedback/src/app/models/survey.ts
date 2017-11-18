import { Question } from './question';
import { SurveyStatus} from './survey-status';
export class Survey {
    id: number;
    status : SurveyStatus;
    name: string;
    description: string;
    questions: Question[];
    labels: string[];
}
