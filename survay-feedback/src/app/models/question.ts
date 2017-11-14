import { Option } from './option';
export class Question {
    id: number;
    text: string;
    options: Option[];
    required: boolean;
}