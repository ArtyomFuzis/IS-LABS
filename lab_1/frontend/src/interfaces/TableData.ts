import type { SelectDTO } from "./DTO/SelectDTO";

export interface TableData<T> {
    data: SelectDTO<T>, 
    filterColumn: string, filterData: string, sortColumn: string, reversedSorting: boolean, 
    setFilterColumn: React.Dispatch<React.SetStateAction<string>>,
    setFilterData: React.Dispatch<React.SetStateAction<string>>,
    setSortColumn: React.Dispatch<React.SetStateAction<string>>, 
    setReversedSorting: React.Dispatch<React.SetStateAction<boolean>>,
    setModalOpen: React.Dispatch<React.SetStateAction<string>>
}