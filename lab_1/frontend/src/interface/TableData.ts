import type { SelectDTO } from "./transfer-data/SelectDTO";

export interface TableData<T> {
    data: SelectDTO<T>, 
    filterColumn: string, filterData: string, sortColumn: string, reversedSorting: boolean, 
    setFilterColumn: React.Dispatch<React.SetStateAction<string>>,
    setFilterData: React.Dispatch<React.SetStateAction<string>>,
    setSortColumn: React.Dispatch<React.SetStateAction<string>>, 
    setReversedSorting: React.Dispatch<React.SetStateAction<boolean>>,
    modalOpen : string, 
    setModalOpen : React.Dispatch<React.SetStateAction<string>>
}