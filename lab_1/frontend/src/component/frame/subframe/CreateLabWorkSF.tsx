import { useEffect, useState } from "react"
import ParameterField from "../frame-component/ParameterField"
import { processIsBiggerThan, processIsNotNull, processIsValidFloat, processObjectChosen, processStringMaxLength } from "../../../util/validations"
import ErrorPanel from "../frame-component/ErrorPanel"
import { makeQueryCreate, makeQueryDelete } from "../../../util/baseMaps"
import { createObject, removeObject } from "../../../util/apiQueries"
import type { LabWork } from "../../../interface/entity/LabWork"
import BaseFrame from "../BaseFrame"
import DeleteAlert from "./DeleteAlert"

function CreateLabWorkSF({ onClose, id, labWork }: { onClose: () => void, id: number, labWork: LabWork|undefined}) {
    const [name, setName]: [any, React.Dispatch<any>] = useState("")
    const [coordinates, setCoordinates]: [any, React.Dispatch<any>] = useState(-1) 
    const [creationDate, setCreationDate]: [any, React.Dispatch<any>] = useState("")
    const [description, setDescription] : [any, React.Dispatch<any>]= useState("")
    const [difficulty, setDifficulty]: [any, React.Dispatch<any>] = useState("")
    const [discipline, setDiscipline]: [any, React.Dispatch<any>] = useState(-1)
    const [minimalPoint, setMinimalPoint]: [any, React.Dispatch<any>] = useState("")
    const [maximumPoint, setMaximumPoint]: [any, React.Dispatch<any>] = useState("")
    const [author, setAuthor]: [any, React.Dispatch<any>] = useState(-1)
    
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    const [alert, setAlert] = useState(false)
    useEffect(() => {
        if(labWork != undefined){
            setName(labWork.name)
            if(labWork.coordinate == null)setCoordinates(-1)
            else setCoordinates(labWork.coordinate.id )
            setCreationDate((new Date(labWork.creationDate+3*3600*1000)).toISOString().slice(0, 16))
            if(labWork.description == null) setDescription("")
            else setDescription(labWork.description)
            if(labWork.difficulty == null) setDifficulty("")
            else setDifficulty(labWork.difficulty?.id)
            if(labWork.discipline == null)setDiscipline(-1)
            else setDiscipline(labWork.discipline.id )
            setMinimalPoint(labWork.minimalPoint?.toString())
            if (labWork.maximalPoint == null) setMaximumPoint("")
            else setMaximumPoint(labWork.maximalPoint?.toString())
            if(labWork.author == null)setAuthor(-1)
            else setAuthor(labWork.author.id )
        }
    },[])

    useEffect(() => {
        console.log(creationDate)
        let res : string[] = []
        processStringMaxLength(name, 400, "Имя", res)
        processIsNotNull(name, "Имя", res)
        processObjectChosen(coordinates, "Координата", res)
        setErrorMsg(res)
        processIsNotNull(description, "Описание", res)
        processIsValidFloat(minimalPoint, false, "Минимальная оценка", res)
        processIsValidFloat(maximumPoint, true, "Максимальная оценка", res)
        processIsBiggerThan(minimalPoint, 0, false, "Минимальная оценка",res)
        processIsBiggerThan(maximumPoint, 0, false, "Максимальная оценка",res)
    }, [name, coordinates, creationDate, description, difficulty, discipline, minimalPoint, maximumPoint, author]);

    function createQuery(){
        let form = new URLSearchParams()
        if(id != -1) form.append('id', id.toString())
        form.append('name', name)
        form.append('coordinate_id', coordinates.toString())
        if(creationDate != "") form.append('creation_date', Date.parse(creationDate).toString())   
        form.append('description', description) 
        if(difficulty != -1) form.append('difficulty_id', difficulty)  
        if(discipline != -1) form.append('discipline_id', discipline.toString()) 
        form.append('minimal_point', minimalPoint)  
        if(maximumPoint != "") form.append('maximal_point', maximumPoint)
        if(author != -1) form.append('author_id', author.toString())      
        createObject(makeQueryCreate("LabWork"),form)
        onClose()
    }

    function deleteQuery(){
        removeObject(makeQueryDelete("LabWork", id.toString()))
        .then(() =>onClose())
        .catch(() => {
            setAlert(true)
        })
    }

    return (
        <>
            <BaseFrame isOpen={alert} onClose={() => setAlert(false)} zindex={5000} width="30%" height="35%">
                <>
                    <DeleteAlert onClose={() => setAlert(false)}></DeleteAlert>
                </>
            </BaseFrame>
            <div className="modal-main-content">
              <div className="modal-header">{id == -1 ? "Создание" : "Изменение"} LabWork</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={coordinates} setValue={setCoordinates} required={true} type="chooseCoordinate" field="Координата" />
                    <ParameterField value={creationDate} setValue={setCreationDate} required={false} type="dateTime" field="Дата создания" />
                    <ParameterField value={description} setValue={setDescription} required={true} type="text" field="Описание" />
                    <ParameterField value={difficulty} setValue={setDifficulty} required={false} type="enumSelectdifficulty" field="Сложность" />
                    <ParameterField value={discipline} setValue={setDiscipline} required={false} type="chooseDiscipline" field="Дисциплина" />
                    <ParameterField value={minimalPoint} setValue={setMinimalPoint} required={true} type="floatNum" field="Минимальная оценка" />
                    <ParameterField value={maximumPoint} setValue={setMaximumPoint} required={false} type="floatNum" field="Максимальная оценка" />
                    <ParameterField value={author} setValue={setAuthor} required={false} type="choosePerson" field="Автор" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                {
                    labWork != undefined && 
                    <button className={`modal-save-button`} onClick={deleteQuery}>
                        Удалить
                    </button>
                }
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    {id == -1 ? "Создать" : "Изменить"}
                </button>
            </div>
        </>
    )
}

export default CreateLabWorkSF