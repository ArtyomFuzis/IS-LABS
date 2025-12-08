import { useEffect, useState } from "react"
import {makeQuerySelect } from "../../../util/baseMaps"
import {fetchData } from "../../../util/apiQueries"
import type { SelectDTO } from "../../../interface/transfer-data/SelectDTO"
import type { TableData } from "../../../interface/TableData"
import CoordinateTable from "../../table/CoordinateTable"
import DisciplineTable from "../../table/DisciplineTable"
import LocationTable from "../../table/LocationTable"
import PersonTable from "../../table/PersonTable"
import LabWorkTable from "../../table/LabWorkTable"
import type { LabWork } from "../../../interface/entity/LabWork"
import type { Person } from "../../../interface/entity/Person"
import type { Coordinate } from "../../../interface/entity/Coordinate"
import type { Discipline } from "../../../interface/entity/Discipline"
import type { Location } from "../../../interface/entity/Location"

function ShowSF({ onClose, objectName, filterColumnEx, filterDataEx}: { onClose: () => void, objectName: string, filterColumnEx: string, filterDataEx: string}) {
    const params = {objectName}
    const [data, setData] = useState({success: false});
    const [filterColumn, setFilterColumn] = useState("");
    const [filterData,   setFilterData]   = useState("");
    const [chosenRow,   setChosenRow]   = useState(-1);
    const [sortColumn, setSortColumn] = useState("");
    const [page, setPage] = useState(1);
    const [reversedSorting, setReversedSorting] = useState(false);
    const [nextPageExists,  setNextPageExists] = useState(false);
    const [modalOpen,  setModalOpen] = useState("");
    const loadData = async () => {
          const result = await fetchData(makeQuerySelect(params.objectName,filterColumnEx,filterDataEx,sortColumn,page,reversedSorting) )
          setData(result)
          const result_next : SelectDTO<any> = await fetchData(makeQuerySelect(params.objectName,filterColumnEx,filterDataEx,sortColumn,page+1,reversedSorting))
          setNextPageExists(result_next.result.length !== 0)
          transfered_data = {data: (data as SelectDTO<any>), filterColumn: filterColumnEx, filterData: filterDataEx, sortColumn, reversedSorting, setFilterColumn, setFilterData, setSortColumn, setReversedSorting, modalOpen, setModalOpen}
     };
    
    let transfered_data : TableData<any> = {data: (data as SelectDTO<any>), filterColumn: filterColumnEx, filterData: filterDataEx, sortColumn, reversedSorting, setFilterColumn, setFilterData, setSortColumn, setReversedSorting, modalOpen, setModalOpen}
    
    useEffect(() => {
        loadData()    
        const interval = setInterval(loadData, 10000)
        return () => clearInterval(interval)
    }, [page, filterColumn, filterData, sortColumn, reversedSorting])

    return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Связанные {objectName}</div>
                <div className="modal-params-container">
                   <div className='table-container'>
                        <div className='table-controllers'>
                        <div className='menu-button-container table-previous-button'>
                            <button className={`menu-button ${page === 1 ? "table-button-inactive" : ""}`} onClick={()=>setPage(page-1)}>
                                Прошлая страница
                            </button>
                        </div>
                        <div className='menu-button-container table-next-button'>
                            <button className={`menu-button ${!nextPageExists ? "table-button-inactive" : ""}`} onClick={()=>{setPage(page+1)}}>
                            Следующая страница
                            </button>
                        </div>
                        </div>
                        {transfered_data !== null && data.success &&
                        <div className='table-body'>
                            {params.objectName == 'Location' && <LocationTable tableData={{...transfered_data, data: (data as SelectDTO<Location>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={false}/>}
                            {params.objectName == 'Discipline' && <DisciplineTable tableData={{...transfered_data, data: (data as SelectDTO<Discipline>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={false}/>}
                            {params.objectName == 'Coordinate' && <CoordinateTable tableData={{...transfered_data, data: (data as SelectDTO<Coordinate>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={false}/>}
                            {params.objectName == 'Person' && <PersonTable tableData={{...transfered_data, data: (data as SelectDTO<Person>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={false}/>}
                            {params.objectName == 'LabWork' && <LabWorkTable tableData={{...transfered_data, data: (data as SelectDTO<LabWork>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} showControls={false}/>}
                        </div>
                        }
                    </div>
                </div>
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
            </div>
        </>
    )
}

export default ShowSF