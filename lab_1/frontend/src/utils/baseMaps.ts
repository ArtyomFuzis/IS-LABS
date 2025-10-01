export const getBaseUrl = (objectName: string) => {
  return {
    "LabWork":    "/operations/labWork",
    "Person":     "/operations/person",
    "Location":   "/operations/location",
    "Discipline": "/operations/discipline",
    "Coordinate": "/operations/coordinate",
  } [objectName]
};

export const getType = (objectName: string) => {
  return {
    "LabWork":    Discip,
    "Person":     "/operations/person",
    "Location":   "/operations/location",
    "Discipline": "/operations/discipline",
    "Coordinate": "/operations/coordinate",
  } [objectName]
};