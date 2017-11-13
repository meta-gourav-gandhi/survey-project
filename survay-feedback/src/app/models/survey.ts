import { Question } from './question';
export class Survey {
    id: number;
    name: string;
    description: string;
    questions: Question[];
    labels: string[];
}
