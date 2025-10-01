import type { Coordinate } from "./Coordinate";
import type { Discipline } from "./Discipline";
import type { EnumVal } from "./EnumVal";

export interface Person {
  id: number,
  name: string|null,
  coordinate: Coordinate|null,
  creationDate: number|null,
  description: string|null,
  difficulty: EnumVal|null,
  discipline: Discipline|null,
  minimalPoint: number|null,
  maximalPoint: number|null,
  author: Person|null
}