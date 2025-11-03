import type { EnumVal } from "./EnumVal";
import type { Location } from "./Location";

export interface Person {
  id: number,
  name: string|null,
  eyeColor: EnumVal|null,
  hairColor: EnumVal|null,
  location: Location|null,
  passportId: string|null,
  nationality: EnumVal|null
}