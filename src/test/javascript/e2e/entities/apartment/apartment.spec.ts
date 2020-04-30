import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ApartmentComponentsPage, ApartmentDeleteDialog, ApartmentUpdatePage } from './apartment.page-object';

const expect = chai.expect;

describe('Apartment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let apartmentComponentsPage: ApartmentComponentsPage;
  let apartmentUpdatePage: ApartmentUpdatePage;
  let apartmentDeleteDialog: ApartmentDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Apartments', async () => {
    await navBarPage.goToEntity('apartment');
    apartmentComponentsPage = new ApartmentComponentsPage();
    await browser.wait(ec.visibilityOf(apartmentComponentsPage.title), 5000);
    expect(await apartmentComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.apartment.home.title');
    await browser.wait(ec.or(ec.visibilityOf(apartmentComponentsPage.entities), ec.visibilityOf(apartmentComponentsPage.noResult)), 1000);
  });

  it('should load create Apartment page', async () => {
    await apartmentComponentsPage.clickOnCreateButton();
    apartmentUpdatePage = new ApartmentUpdatePage();
    expect(await apartmentUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.apartment.home.createOrEditLabel');
    await apartmentUpdatePage.cancel();
  });

  it('should create and save Apartments', async () => {
    const nbButtonsBeforeCreate = await apartmentComponentsPage.countDeleteButtons();

    await apartmentComponentsPage.clickOnCreateButton();

    await promise.all([apartmentUpdatePage.setUnitNumberInput('unitNumber'), apartmentUpdatePage.buildingSelectLastOption()]);

    expect(await apartmentUpdatePage.getUnitNumberInput()).to.eq('unitNumber', 'Expected UnitNumber value to be equals to unitNumber');
    const selectedMoveInReady = apartmentUpdatePage.getMoveInReadyInput();
    if (await selectedMoveInReady.isSelected()) {
      await apartmentUpdatePage.getMoveInReadyInput().click();
      expect(await apartmentUpdatePage.getMoveInReadyInput().isSelected(), 'Expected moveInReady not to be selected').to.be.false;
    } else {
      await apartmentUpdatePage.getMoveInReadyInput().click();
      expect(await apartmentUpdatePage.getMoveInReadyInput().isSelected(), 'Expected moveInReady to be selected').to.be.true;
    }

    await apartmentUpdatePage.save();
    expect(await apartmentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await apartmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Apartment', async () => {
    const nbButtonsBeforeDelete = await apartmentComponentsPage.countDeleteButtons();
    await apartmentComponentsPage.clickOnLastDeleteButton();

    apartmentDeleteDialog = new ApartmentDeleteDialog();
    expect(await apartmentDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.apartment.delete.question');
    await apartmentDeleteDialog.clickOnConfirmButton();

    expect(await apartmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
