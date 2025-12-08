

export const getBaseUrl = (objectName: string): string => {
  return {
    "LabWork":    "/operations/labWork",
    "Person":     "/operations/person",
    "Location":   "/operations/location",
    "Discipline": "/operations/discipline",
    "Coordinate": "/operations/coordinate",
  } [objectName] || ""
};

const getBaseMapping = (objectName: string): string => {
  return ({
    "name":         "/name/",
    "id":           "/id/",
    "x":            "/x/",
    "y":            "/y/",
    "z":            "/z/",
    "labsCount":    "/labsCount/",
    "eyeColor":     "/eyeColor.val/",
    "hairColor":    "/hairColor.val/",
    "locationId":   "/location.id/",
    "passport":     "/passportId/",
    "nationality":  "/nationality.val/",
    "coordinateId": "/coordinate.id/",
    "creationDate": "/creationDate/",
    "description":  "/description/",
    "difficulty":   "/difficulty.val/",
    "disciplineId": "/discipline.id/",
    "minimalPoint": "/minimalPoint/",
    "maximalPoint": "/maximalPoint/",
    "authorId":     "/author.id/"
  } [objectName] || "")
}

export const getFilterUrl = (objectName: string, pageNum: number, filter:string): string => {
   return "/filtered" + getBaseMapping(objectName) + pageNum + "/?filter="+filter
};

export const getSortUrl = (objectName: string, pageNum: number, reversed: boolean): string => {
  return "/sorted" + getBaseMapping(objectName) + pageNum + "/?reversed=" + reversed
};

export const makeQuerySelect = (objectName : string, filterColumn: string, filterData: string, sortColumn: string, page: number, reversedSorting: boolean) => {
  return getBaseUrl(objectName) + 
        ((filterColumn!=="") ? getFilterUrl(filterColumn, page, filterData) : 
        ((sortColumn!=="") ? getSortUrl(sortColumn, page, reversedSorting) : "/page/"+page))
};

export const makeQueryCreate = (objectName: string) => {
  return getBaseUrl(objectName) + "/"
}


export const makeQueryEnumValGet = (enumName: string) => {
  return "/EnumVals/" + enumName
}


export const makeQueryDelete = (objectName: string ,id:string) => {
  return getBaseUrl(objectName) + "/" + id
}