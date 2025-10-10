

export const getBaseUrl = (objectName: string): string => {
  return {
    "LabWork":    "/operations/labWork",
    "Person":     "/operations/person",
    "Location":   "/operations/location",
    "Discipline": "/operations/discipline",
    "Coordinate": "/operations/coordinate",

  } [objectName] || ""
};

export const getFilterUrl = (filteredName: string, pageNum: number, filter:string): string => {
  return ("/get/filtered" + {
    "name":       "/name/"+pageNum,
    "labwork":    "/lab/"+pageNum

  } [filteredName] || "") + "/?filter="+filter
};

export const getSortUrl = (objectName: string, pageNum: number, reversed: boolean): string => {
  return ("/get/sorted" + {
    "name":       "/name/"+pageNum,
    "labwork":    "/lab/"+pageNum

  } [objectName] || "")+"/?reversed="+reversed
};


export const makeQuery = (objectName : string, filterColumn: string, filterData: string, sortColumn: string, page: number, reversedSorting: boolean) => {
  return getBaseUrl(objectName) + 
        ((filterColumn!=="") ? getFilterUrl(filterColumn, page, filterData) : 
        ((sortColumn!=="") ? getSortUrl(sortColumn, page, reversedSorting) : "/get/page/"+page))
};


